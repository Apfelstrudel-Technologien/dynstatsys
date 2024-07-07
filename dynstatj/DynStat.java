// Apfelstrudel Technologien Dynamic States System.

package dynstatj;

import java.util.ArrayList;

/**
 * Dynamic states system.
 */
public class DynStat {
    private float stat, maxStat, minStat;

    private ArrayList<DynStatEffect> effects;

    public static final int FIRST_EFFECT = -0x1;
    public static final int LATEST_EFFECT = -0x2;

    public static final int NO_ID = -0x3;

    /**
     * Initialize dynamic states system.
     * 
     * @param stat_ Current stat value.
     * @param maxStat_ Maximal stat value.
     * @param minStat_ Minimal stat value.
     */
    public DynStat(float stat_, float maxStat_, float minStat_) {
        effects = new ArrayList<>();

        stat = stat_;

        maxStat = maxStat_;

        minStat = minStat_;
    }

    /**
     * Initialize dynamic states system with maximal stat value as current value.
     * 
     * @param maxStat_ Maximal stat value.
     * @param minStat_ Minimal stat value.
     */
    public DynStat(float maxStat_, float minStat_) {
        this(maxStat_, maxStat_, minStat_);
    }

    /**
     * Get current stat.
     */
    public float getStat() {
        return stat;
    }

    /**
     * Get current state rounded.
     */
    public int getStatRndf() {
        return Math.round(stat);
    }

    /**
     * Get maximal stat.
     */
    public float getMaxStat() {
        return maxStat;
    }

    /**
     * Get minimal stat.
     */
    public float getMinStat() {
        return minStat;
    }

    /**
     * Get effect count (effects size).
     */
    public int getEffectCount() {
        return effects.size();
    }

    /**
     * Get effects array.
     */
    public DynStatEffect[] getEffects() {
        return effects.toArray(new DynStatEffect[0]);
    }

    /**
     * Is current stat value is at it's maximal value.
     */
    public boolean isMax() {
        return stat >= maxStat;
    }

    /**
     * Is current stat value is at it's minimal value.
     */
    public boolean isMin() {
        return stat <= minStat;
    }

    /**
     * Update current stat value.
     * 
     * @param stat_ New stat value.
     */
    public void setStat(float stat_) {
        stat = (stat_ > maxStat) ? maxStat : (stat_ < minStat) ? minStat : stat_;
    }

    /**
     * Add value to current stat.
     * 
     * @param add To add.
     */
    public void addStat(float add) {
        setStat(stat + add);
    }

    /**
     * Subtract value from current stat.
     * 
     * @param sub To subtract.
     */
    public void subStat(float sub) {
        addStat(-sub);
    }

    /**
     * Get effect by its ID.
     * 
     * @param id ID.
     */
    public DynStatEffect getEffectByID(int id) {
        for(DynStatEffect effect : effects) {
            if(effect.id == id) {
                return effect;
            }
        }

        return null;
    }

    /**
     * Is effect count more than 0.
     */
    public boolean hasAnyEffect() {
        return getEffectCount() > 0;
    }

    /**
     * Is system has effect with following ID.
     * 
     * @param id ID.
     */
    public boolean hasEffectWithID(int id) {
        return getEffectByID(id) != null;
    }

    /**
     * Set effect active state.
     * 
     * @param effInd Effect index.
     * @param active Is active?
     */
    public void effectSetActive(int effInd, boolean active) {
        effInd = (effInd == FIRST_EFFECT) ? 0 : (effInd == LATEST_EFFECT) ? getEffectCount() - 1 : effInd;

        if (effInd < 0 || effInd >= getEffectCount()) return;

        effects.get(effInd).active = active;
    }

    /**
     * Active effect.
     * 
     * @param effInd Effect index.
     */
    public void effectEnable(int effInd) {
        effectSetActive(effInd, true);
    }

    /**
     * Disable effect.
     * 
     * @param effInd Effect index.
     */
    public void effectDisable(int effInd) {
        effectSetActive(effInd, false);
    }

    /**
     * Set effect negative multiplication modificator.
     * 
     * @param effInd Effect index.
     * @param statNegMultMod Stat negative multiplication modificator.
     */
    public void setEffectNegMultMod(int effInd, float statNegMultMod) {
        effInd = (effInd == FIRST_EFFECT) ? 0 : (effInd == LATEST_EFFECT) ? getEffectCount() - 1 : effInd;

        if(effInd < 0 || effInd >= getEffectCount()) return;

        effects.get(effInd).statNegMultMod = statNegMultMod;
    }

    /**
     * Register effect in system.
     * 
     * @param effect Effect.
     */
    public void addEffect(DynStatEffect effect) {
        effect.id = effect.id == NO_ID ? getEffectCount() + 1 : effect.id;

        effects.add(effect);
    }

    /**
     * Remove effect from system.
     * 
     * @param effInd Effect index.
     */
    public void remEffect(int effInd) {
        effInd = (effInd == FIRST_EFFECT) ? 0 : (effInd == LATEST_EFFECT) ? getEffectCount() - 1 : effInd;

        if(effInd < 0 || effInd >= getEffectCount()) return;

        effects.remove(effInd);
    }

    /**
     * Process effects.
     */
    public void proc() {
        for(DynStatEffect effect : effects) {
            if(effect.active) setStat(effect.procEffect.apply(stat, effect.statNegMultMod));
        }
    }

    /**
     * Apply modificator for stat.
     * 
     * @param stat Stat value.
     * @param mod Modificator.
     */
    public static float applyMod(float stat, float mod) {
        return stat * mod;
    }
}
