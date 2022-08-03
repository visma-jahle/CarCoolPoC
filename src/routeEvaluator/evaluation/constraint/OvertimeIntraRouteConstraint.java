package routeEvaluator.evaluation.constraint;

import model.Model;
import model.Shift;
import routeEvaluator.evaluation.OvertimeAbstract;
import routeEvaluator.evaluation.info.ConstraintInfo;

/**
 * Intra route overtime constraint. Allows overtime to be handled for the individual shifts such that a shift has
 * a maximum number of seconds of overtime allowed. If none is given the default allowed overtime is give by the parameter
 * DEFAULT_MAXIMUM_OVERTIME.
 */
public class OvertimeIntraRouteConstraint extends OvertimeAbstract implements IConstraintIntraRoute {

    private static final int DEFAULT_MAXIMUM_OVERTIME = 0;
    private Integer[] shiftMaxOvertime;

    public OvertimeIntraRouteConstraint(Model model) {
        shiftMaxOvertime = new Integer[model.getShifts().size()];
        for (Shift shift : model.getShifts()) {
            shiftMaxOvertime[shift.getId()] = getLatestAllowedEndOfShift(model, shift);
        }
    }

    private Integer getLatestAllowedEndOfShift(Model model, Shift shift) {
        return (model.getMaximumOvertime() != null ?
                model.getMaximumOvertime().getOrDefault(shift, DEFAULT_MAXIMUM_OVERTIME) : DEFAULT_MAXIMUM_OVERTIME)
                + shift.getEndTime();
    }
    
    @Override
    public boolean constraintIsFeasible(ConstraintInfo constraintInfo) {
        return !isOverTime(shiftMaxOvertime[constraintInfo
                .getShiftId()], constraintInfo.getEarliestOfficeReturn());
    }


}