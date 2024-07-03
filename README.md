<h1 align="center">DynStatC/J</h1>
The DynStat Library is a lightweight, flexible utility for simulating dynamic stats (such as health, stamina, etc.) in games. It is designed to be framework-agnostic, meaning it can be integrated seamlessly with any game engine or framework without requiring any specific dependencies. Also supports two languages: C and Java.
<br><br>

```C``` Cheatsheet:
```c
#define FIRST_EFFECT -0x1
#define LATEST_EFFECT -0x2

#define DYNSTAT_VERSION 1.00

typedef struct {
    float stat, maxStat, minStat, statNegMultMod;

    int effectCount;

    struct DynStatEffect *effects;
} DynStat;

typedef struct DynStatEffect {
    void (*procEffect) (float *rStat, float statNegMultMod);

    float statNegMultMod;

    bool active;
} DynStatEffect;

void dynstatInit(DynStat *dynstat, float stat, float max, float min)
void dynstatInitSh(DynStat *dynstat, float max, float min)

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

int dynstatHasAnyEffect(DynStat *dynstat)

void dynstatEffectSetActive(DynStat *dynstat, int effInd, bool active)

void dynstatEffectEnable(DynStat *dynstat, int effInd)
void dynstatEffectDisable(DynStat *dynstat, int effInd)

void dynstatSetEffectNegMultMod(DynStat *dynstat, int effInd, float statNegMultMod)

void dynstatAddEffect(DynStat *dynstat, DynStatEffect effect)
void dynstatRemEffect(DynStat *dynstat, int effInd)

void dynstatProc(DynStat *dynstat)

void dynstatFree(DynStat *dynstat)
```

```c
#include "dynstat.c"

#include <stdio.h>

void bleedingEffect(float *rStat, float statNegMultMod) {
    *rStat -= 1 * statNegMultMod;
}

int main() {
    DynStat dynstat;

    dynstatInitSh(&dynstat, 100, 0);

    DynStatEffect bleed = {bleedingEffect, 0.001f, true};

    dynstatAddEffect(&dynstat, bleed);

    while(true) {
        dynstatProc(&dynstat);

        printf("%d\n", dynstatGetStatRndf(&dynstat));
    }

    dynstatFree(&dynstat);

    return 0;
}
```
