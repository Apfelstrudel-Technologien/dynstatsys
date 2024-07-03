// Apfelstrudel Technologien Dynamic Stats System.

#include <stdbool.h>
#include <stdlib.h>
#include <math.h>

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

void dynstatInit(DynStat *dynstat, float stat, float max, float min) {
    dynstat->stat = stat;
    dynstat->maxStat = max;
    dynstat->minStat = min;
    dynstat->statNegMultMod = 0.1f;
    dynstat->effectCount = 0;
    dynstat->effects = NULL;
}

void dynstatInitSh(DynStat *dynstat, float max, float min) {
    dynstatInit(dynstat, max, max, min);
}

float dynstatGetStat(DynStat *dynstat) { return dynstat->stat; }
int dynstatGetStatRndf(DynStat *dynstat) { return (int) roundf(dynstat->stat); }
float dynstatGetMaxStat(DynStat *dynstat) { return dynstat->maxStat; }
float dynstatGetMinStat(DynStat *dynstat) { return dynstat->minStat; }
int dynstatGetEffectCount(DynStat *dynstat) { return dynstat->effectCount; }
DynStatEffect *dynstatGetEffects(DynStat *dynstat) { return dynstat->effects; }
bool dynstatIsMax(DynStat *dynstat) { return dynstat->stat >= dynstat->maxStat; }
bool dynstatIsMin(DynStat *dynstat) { return dynstat->stat <= dynstat->minStat; }

void dynstatSetStat(DynStat *dynstat, float stat) { dynstat->stat = (stat > dynstat->maxStat) ? dynstat->maxStat : (stat < dynstat->minStat) ? dynstat->minStat : stat; }
void dynstatAddStat(DynStat *dynstat, float stat) { dynstatSetStat(dynstat, dynstat->stat + stat); }
void dynstatSubStat(DynStat *dynstat, float stat) { dynstatSetStat(dynstat, dynstat->stat - stat); }

int dynstatHasAnyEffect(DynStat *dynstat) { return dynstat->effectCount > 0; }

void dynstatEffectSetActive(DynStat *dynstat, int effInd, bool active) {
    effInd = (effInd == FIRST_EFFECT) ? 0 : (effInd == LATEST_EFFECT) ? dynstat->effectCount - 1 : effInd;

    if(effInd < 0 || effInd >= dynstat->effectCount) return;

    dynstat->effects[effInd].active = active;
}

void dynstatEffectEnable(DynStat *dynstat, int effInd) {
    dynstatEffectSetActive(dynstat, effInd, true);
}

void dynstatEffectDisable(DynStat *dynstat, int effInd) {
    dynstatEffectSetActive(dynstat, effInd, false);
}

void dynstatSetEffectNegMultMod(DynStat *dynstat, int effInd, float statNegMultMod) {
    effInd = (effInd == FIRST_EFFECT) ? 0 : (effInd == LATEST_EFFECT) ? dynstat->effectCount - 1 : effInd;

    if(effInd < 0 || effInd >= dynstat->effectCount) return;

    dynstat->effects[effInd].statNegMultMod = statNegMultMod;
}

void dynstatAddEffect(DynStat *dynstat, DynStatEffect effect) {
    dynstat->effectCount++;

    dynstat->effects = realloc(dynstat->effects, dynstat->effectCount * sizeof(DynStatEffect));
    dynstat->effects[dynstat->effectCount - 1] = effect;
}

void dynstatRemEffect(DynStat *dynstat, int effInd) {
    effInd = (effInd == FIRST_EFFECT) ? 0 : (effInd == LATEST_EFFECT) ? dynstat->effectCount - 1 : effInd;

    if(effInd < 0 || effInd >= dynstat->effectCount) return;

    for(int index = effInd; index < dynstat->effectCount - 1; index++) {
        dynstat->effects[index] = dynstat->effects[index + 1];
    }

    dynstat->effectCount--;

    dynstat->effects = realloc(dynstat->effects, dynstat->effectCount * sizeof(DynStatEffect));
}

void dynstatProc(DynStat *dynstat) {
    for(int effInd = 0; effInd < dynstat->effectCount; effInd++) {
        if(dynstat->effects[effInd].active) {
            dynstat->effects[effInd].procEffect(&(dynstat->stat), dynstat->effects[effInd].statNegMultMod);

            dynstat->stat = (dynstat->stat > dynstat->maxStat) ?
                dynstat->maxStat : (dynstat->stat < dynstat->minStat) ?
                    dynstat->minStat : dynstat->stat;
        }
    }
}

void dynstatFree(DynStat *dynstat) { free(dynstat->effects); dynstat->effects = NULL; }
