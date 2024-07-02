<h1 align="center">DynStatC/J</h1>
The DynStat Library is a lightweight, flexible utility for simulating dynamic stats (such as health, stamina, etc.) in games. It is designed to be framework-agnostic, meaning it can be integrated seamlessly with any game engine or framework without requiring any specific dependencies. Also supports two languages: C and Java.
<br><br>

```C``` Cheatsheet:
```c
#define FIRST_EFFECT 0x16
#define LATEST_EFFECT 0x32

#define DYNSTAT_VERSION 1.00

typedef struct {
    float stat, maxStat, minStat, delta;

    int effectCount;

    struct DynStatEffect *effects;
} DynStat;

typedef struct DynStatEffect {
    void (*procEffect) (float *rStat, float delta);

    float statNegMultMod;
} DynStatEffect;

float dynstatGetStat(DynStat *dynstat)
int dynstatGetStatRndf(DynStat *dynstat)
float dynstatGetMaxStat(DynStat *dynstat)
float dynstatGetMinStat(DynStat *dynstat)
int dynstatGetEffectCount(DynStat *dynstat)
DynStatEffect *dynstatGetEffects(DynStat *dynstat)
bool dynstatIsMin(DynStat *dynstat)

void dynstatSetStat(DynStat *dynstat, float stat)
void dynstatAddStat(DynStat *dynstat, float stat)
void dynstatSubStat(DynStat *dynstat, float stat)

int dynstatHasAnyEffect(DynStat *dynstat)

void dynstatAddEffect(DynStat *dynstat, DynStatEffect effect)

void dynstatRemEffect(DynStat *dynstat, int effInd)

void dynstatProc(DynStat *dynstat)

void dynstatFreeEffects(DynStat *dynstat)
```

```c
#include "dynstat.c"

#include <stdio.h>

void bleedingEffect(float *rStat, float delta) {
    *rStat -= 1 * delta;
}

int main() {
    DynStat dynstat = {100, 100, 0, 0, 0, NULL};

    DynStatEffect bleed = {bleedingEffect, 0.0001f};

    dynstatAddEffect(&dynstat, bleed);

    while(true) {
        dynstatProc(&dynstat);

        printf("%d\n", dynstatGetStatRndf(&dynstat));
    }

    dynstatFreeEffects(&dynstat);

    return 0;
}
```
