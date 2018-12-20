package com.yootii.bdy.material.service;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.material.model.MaterialSort;

public interface MaterialSortService {

	ReturnInfo queryMaterialSort(MaterialSort materialSort, GeneralCondition gcon);

	ReturnInfo createMaterialSort(MaterialSort materialSort, GeneralCondition gcon);

	ReturnInfo deleteMaterialSort(MaterialSort materialSort, GeneralCondition gcon);

	ReturnInfo modifyMaterialSort(MaterialSort materialSort, GeneralCondition gcon);

}
