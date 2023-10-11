package com.nishant.code_it.code_exec_engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.nishant.code_it.enums.Language;

@Service
public class CodeGeneratorFactory {

	@Autowired
	private ApplicationContext context;
	
	public CodeGenerator getCodeGenerator(Language language)
	{
	    if(language.equals(Language.JAVA))
	    	return context.getBean(JavaCodeGenerator.class);
	   
	    return null;
	}
}
