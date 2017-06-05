package sk.onkokubo.iot.tradfriandroid.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ondrejkubo on 04/06/2017.
 */

public final class TradfriIdListModel implements TradfriEntityContainer {

    private List<Long> mIds = new ArrayList<>();

    public List<Long> getmIds() {
        return mIds;
    }

    public void setmIds(List<Long> ids) {
        this.mIds = ids;
    }
}
