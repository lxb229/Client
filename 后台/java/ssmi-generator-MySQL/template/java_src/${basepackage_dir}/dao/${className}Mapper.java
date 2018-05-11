<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao.${branchpackage};


import com.guse.platform.common.base.BaseMapper;
import com.guse.platform.entity.doudou.${className};


/**
 * ${table.sqlName}
 * @see ${className}Mapper.xml
 * @author nbin
 * @date 2017年7月18日 下午2:02:28 
 * @version V1.0
 */
public interface ${className}Mapper extends  BaseMapper<${className}, ${table.idColumn.javaType}>{
	
}
