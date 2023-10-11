package com.nishant.code_it.services;

import org.springframework.stereotype.Service;

import com.nishant.code_it.enums.Language;

@Service
public class LanguageService {
  
	
	public Language getLanguage(String language)
	{
	     language = language.toUpperCase();
	     
	     switch(language) {
	     case "JAVA":
	        return Language.JAVA;
	       case "PYTHON":
	       return Language.PYTHON;
	       default:
	       return Language.CPP;
	   }

	     
	}
}
