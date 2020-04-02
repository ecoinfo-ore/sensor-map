
package com.rac021.sensormap.api.root;

import java.io.Serializable ;
import javax.persistence.Id ;
import javax.persistence.Entity ;

/**
 *
 * @author ryahiaoui
 */

// Used to Enable JPA
@Entity
class DbEntity implements Serializable {

    @Id
    Integer id = 0 ;
    
}