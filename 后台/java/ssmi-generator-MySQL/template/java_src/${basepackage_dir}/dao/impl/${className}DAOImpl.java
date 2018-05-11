<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.dao.impl;


import org.springframework.stereotype.Repository;

import com.mftour.moudles.dao.I${className}DAO;
import com.mftour.moudles.entity.$


/**
 * 
 * @author dqzhai
 * @see ${className}Mapper.xml
 * @version 1.0
 * @CreateDate 2015-7-26 - 上午11:34:26
 */
@Repository("${classNameLower}Dao")
public class ${className}DAOImpl extends BaseEntityDAO<${className},${table.idColumn.javaType}> implements I${className}DAO{
	
	static final String CURRENT_NAMESPACE = "${className}Mapper" ;
	static final String CUURENT_PREFIX = "";
	
	@Override
	public void prepareForSave(${className} entity) {
		if (StringUtils.isBlank(entity.getId())) {
			entity.setId(IdGeneratorUtil.getId(CUURENT_PREFIX));
		}
//		entity.setCreatedAt(new Date());
	}

	@Override
	public void prepareForUpdate(${className} entity) {
	}
	
	
	@Override
	public String getIbatisSqlMapNamespace() {
		return CURRENT_NAMESPACE;
	}
	
	
	
}
