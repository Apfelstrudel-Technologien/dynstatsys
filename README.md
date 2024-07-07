<h1 align="center">DynStatC/J</h1>
The Apfelstrudel Technologien Dynamic Statistics System (DynStat) is a C library designed to manage dynamic statistics with ease and flexibility. It allows the initialization, manipulation, and application of various effects on statistics in a structured and efficient manner. This library is suitable for applications in gaming, simulations, and any other domain where dynamic and reactive statistical management is required.
<br>

```C``` Cheatsheet:
```c
#define DS_FIRST_EFFECT -0x1
#define DS_LATEST_EFFECT -0x2

#define DS_NO_ID -0x3

typedef struct {
    float stat, maxStat, minStat;

    int effectCount;

    struct DynStatEffect *effects;
} DynStat;

typedef struct DynStatEffect {
    void (*procEffect) (float *rStat, float statNegMultMod);

    float statNegMultMod;

    bool active; int id;
} DynStatEffect;

void dynstatInit(DynStat *dynstat, float stat, float max, float min)
void dynstatInitSh(DynStat *dynstat, float max, float min)

void dynstatInitEffect(DynStatEffect *effect, void (*procEffect) (float *rStat, float statNegMultMod), float statNegMultMod, bool active, int id)
void dynstatInitEffectSh(DynStatEffect *effect, void (*procEffect) (float *rStat, float statNegMultMod), float statNegMultMod)

float dynstatGetStat(DynStat *dynstat)
int dynstatGetStatRndf(DynStat *dynstat)
float dynstatGetMaxStat(DynStat *dynstat)
float dynstatGetMinStat(DynStat *dynstat)

int dynstatGetEffectCount(DynStat *dynstat)
DynStatEffect *dynstatGetEffects(DynStat *dynstat)

bool dynstatIsMax(DynStat *dynstat)
bool dynstatIsMin(DynStat *dynstat)

void dynstatSetStat(DynStat *dynstat, float stat)
void dynstatAddStat(DynStat *dynstat, float stat)
void dynstatSubStat(DynStat *dynstat, float stat)

DynStatEffect *dynstatGetEffectID(DynStat *dynstat, int id)

bool dynstatHasAnyEffect(DynStat *dynstat)
bool dynstatHasEffectWithID(DynStat *dynstat, int id)

void dynstatEffectSetActive(DynStat *dynstat, int effInd, bool active)

void dynstatEffectEnable(DynStat *dynstat, int effInd)
void dynstatEffectDisable(DynStat *dynstat, int effInd)

void dynstatSetEffectNegMultMod(DynStat *dynstat, int effInd, float statNegMultMod)
void dynstatAddEffect(DynStat *dynstat, DynStatEffect effect)

void dynstatRemEffect(DynStat *dynstat, int effInd)

void dynstatProc(DynStat *dynstat)

float dynstatApplyMod(float stat, float mod)

void dynstatFree(DynStat *dynstat)
```

```c
#include "dynstat.c"

#include <stdio.h>

void bleedingEffect(float *rStat, float statNegMultMod) { *rStat -= dynstatApplyMod(1, statNegMultMod); }

int main() {
    DynStat dynstat; dynstatInitSh(&dynstat, 100, 0);

    DynStatEffect bleed; dynstatInitEffectSh(&bleed, bleedingEffect, 0.0001f);

    dynstatAddEffect(&dynstat, bleed);

    while(true) { dynstatProc(&dynstat);
                  printf("%d\n", dynstatGetStatRndf(&dynstat)); }

    dynstatFree(&dynstat); return 0;
}
```

Java:
```java
import dynstatj.DynStat;
import dynstatj.DynStatEffect;

public class Main {
    public static void main(String[] args) {
        DynStat dynstat = new DynStat(100, 0);

        dynstat.addEffect(new DynStatEffect((stat, mod) -> { return stat - (1 * mod); }, 0.0001f));

        while(true) {
            dynstat.proc();

            System.out.println(dynstat.getStatRndf());
        }
    }
}
```
