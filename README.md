# Shared Kernel | DDD Strategic Pattern Example

[![Pattern](https://img.youtube.com/vi/VomWurwjwqA/0.jpg)](https://www.youtube.com/watch?v=VomWurwjwqA)

## ¿Qué es un Shared Kernel?

En Domain-Driven Design, un **Shared Kernel** es un patrón estratégico que define un subconjunto pequeño y explícitamente delimitado del modelo de dominio que dos o más bounded contexts acuerdan compartir. A diferencia de otros patrones de integración donde los contextos se comunican mediante contratos o capas de traducción, el Shared Kernel establece un **código común** que múltiples equipos co-mantienen.

Eric Evans presenta este patrón como una decisión deliberada: cuando el costo de traducir entre dos modelos supera el costo de mantener un subconjunto compartido, el Shared Kernel se convierte en la opción pragmática.

## Cuándo usarlo

- Cuando dos o más bounded contexts comparten conceptos centrales que son **semánticamente idénticos** (no solo estructuralmente similares).
- Cuando mantener representaciones separadas del mismo concepto introduce **complejidad accidental** — mapeos redundantes, bugs divergentes, overhead de sincronización.
- Cuando los conceptos compartidos son **estables y bien comprendidos**. Las áreas volátiles del dominio son malos candidatos porque cada cambio se propaga a todos los consumidores.
- Cuando los equipos involucrados pueden comprometerse con un **modelo de gobernanza conjunta** sobre el código compartido — reviews coordinados, disciplina de versionado y ownership claro.

## Cuándo NO usarlo

- Cuando los conceptos solo parecen similares en la superficie pero llevan **semánticas distintas** en cada contexto (ej: "Account" en facturación vs. "Account" en identidad). Esto es un falso cognado — usá modelos separados con una Anti-Corruption Layer.
- Cuando los equipos operan con **cadencias de release diferentes** o tienen baja confianza/comunicación. Un Shared Kernel exige coordinación; sin ella, terminás con un monolito distribuido.
- Cuando la superficie compartida **no para de crecer**. Un Shared Kernel debe ser mínimo. Si tiende a absorber lógica de dominio significativa, probablemente estés fusionando contextos en lugar de compartir un kernel.

## Restricciones de diseño

Un Shared Kernel bien diseñado sigue reglas estrictas:

1. **Minimalidad** — Solo incluir tipos que genuinamente pertenecen a ambos contextos. Cada tipo que se agrega incrementa el acoplamiento; cada tipo que se omite preserva la autonomía.
2. **Inmutabilidad por convención** — Los tipos compartidos deben comportarse como Value Objects. Esto elimina una categoría entera de bugs de concurrencia y aliasing, y hace que el código compartido sea seguro de razonar en aislamiento.
3. **Sin cambios unilaterales** — Cualquier modificación al kernel requiere acuerdo de todos los equipos consumidores. Es un contrato social respaldado por CI: los breaking changes deben detectarse temprano.
4. **Frontera explícita** — El kernel vive en un paquete o módulo dedicado (`sharedkernel`). No debe haber ambigüedad sobre qué es compartido y qué no.

## Este ejemplo

Este proyecto modela un dominio financiero donde tres bounded contexts — **Trading**, **Portfolio** y **Risk** — comparten un vocabulario común:

```
sharedkernel/
├── AccountId.java    → Identidad (Value Object que envuelve UUID)
├── Currency.java     → Enum con las monedas soportadas
└── Money.java        → Value Object con aritmética y validación de moneda
```

Cada bounded context depende del shared kernel pero **no entre sí**:

```
trading/domain/TradeOrder       ──→  sharedkernel/Money, AccountId
portfolio/domain/Position       ──→  sharedkernel/Money
risk/domain/RiskExposure        ──→  sharedkernel/Money
```

Esta estructura impone un invariante arquitectónico clave: **los contextos se acoplan únicamente a través del kernel, nunca de forma directa**. Si `TradeOrder` alguna vez necesitara referenciar a `Position`, eso señalaría un concepto faltante en el kernel o un boundary mal ubicado — ambos merecen investigación antes de escribir el import.

## Tradeoffs a considerar

| Beneficio | Costo |
|---|---|
| Elimina traducción redundante de modelos | Introduce acoplamiento fuerte entre contextos |
| Fuente única de verdad para conceptos compartidos | Requiere coordinación cross-team para cambios |
| Reduce bugs de mapeo y deriva | Cambios en el kernel pueden romper múltiples consumidores |
| Más simple que mantener ACLs para tipos estables | La tentación de sobre-compartir crece con el tiempo |

El Shared Kernel no es una elección por defecto — es un **tradeoff deliberado**. En la mayoría de los sistemas, los bounded contexts deberían preferir la autonomía (Separate Ways, Customer-Supplier o Conformist). Reservá el Shared Kernel para el conjunto reducido de conceptos donde la propiedad compartida genuinamente cuesta menos que la independencia.
