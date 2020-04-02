/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rac021.sensormap.api.qualifiers;

import java.lang.annotation.Target ;
import java.lang.annotation.Retention ;
import javax.transaction.Transactional ;
import java.lang.annotation.ElementType ;
import javax.enterprise.inject.Stereotype ;
import java.lang.annotation.RetentionPolicy ;
import javax.enterprise.context.RequestScoped ;

/**
 *
 * @author ryahiaoui
 */
@Stereotype
@Transactional
@RequestScoped
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Stateless {}    
