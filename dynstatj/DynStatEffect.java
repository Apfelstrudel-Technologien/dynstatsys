package dynstatj;

import java.util.function.BiFunction;

/**
 * Dynamic statistic effect.
 */
public class DynStatEffect {
    public BiFunction<Float, Float, Float> procEffect;

    public float statNegMultMod;

    public boolean active;

    public int id;

    /**
     * Initialize dynamic state effect.
     * 
     * @param procEffect_ Processing effect.
     * @param statNegMultMod_ Stat negative multiplication modificator.
     * @param active_ Is effect active?
     * @param id_ Effect ID.
     */
    public DynStatEffect(BiFunction<Float, Float, Float> procEffect_, float statNegMultMod_, boolean active_, int id_) {
        procEffect = procEffect_;

        statNegMultMod = statNegMultMod_;

        active = active_;

        id = id_;
    }

    /**
     * Initialize dynamic state effect without ID.
     * 
     * @param procEffect Processing effect.
     * @param statNegMultMod Stat negative multiplication modificator.
     * @param active Is effect active?
     */
    public DynStatEffect(BiFunction<Float, Float, Float> procEffect, float statNegMultMod, boolean active) {
        this(procEffect, statNegMultMod, active, DynStat.NO_ID);
    }

    /**
     * Initialize dynamic state effect without ID and activitity set to true.
     * 
     * @param procEffect Processing effect.
     * @param statNegMultMod Stat negative multiplication modificator.
     */
    public DynStatEffect(BiFunction<Float, Float, Float> procEffect, float statNegMultMod) {
        this(procEffect, statNegMultMod, true, DynStat.NO_ID);
    }
}
