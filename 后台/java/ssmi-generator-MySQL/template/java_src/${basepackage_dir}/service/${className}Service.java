<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.service.${branchpackage};

import com.guse.platform.common.base.BaseService;
import com.guse.platform.entity.doudou.${className};

/**
 * ${table.sqlName}
 * @author nbin
 * @date 2017年7月18日 下午2:14:09 
 * @version V1.0
 */
public interface ${className}Service extends BaseService<${className},${table.idColumn.javaType}>{

	
}
