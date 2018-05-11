<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.${branchpackage}.impl;

//
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guse.platform.common.base.BaseServiceImpl;
import com.guse.platform.dao.doudou.${className}Mapper;
import com.guse.platform.service.doudou.${className}Service;
import com.guse.platform.entity.doudou.${className};

/**
 * ${table.sqlName}
 * @author nbin
 * @date 2017年8月1日 下午4:09:56 
 * @version V1.0
 */
@Service
public class ${className}ServiceImpl extends BaseServiceImpl<${className}, ${table.idColumn.javaType}> implements ${className}Service{

	@Autowired
	private ${className}Mapper  ${classNameLower}Mapper;
	
	@Autowired
	public void setBaseMapper(){
	   super.setBaseMapper(${classNameLower}Mapper);
	}
}
