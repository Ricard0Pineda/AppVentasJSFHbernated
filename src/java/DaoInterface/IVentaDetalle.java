/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DaoInterface;

import Pojos.Tventadetalle;
import org.hibernate.Session;

/**
 *
 * @author Ricardo
 */
public interface IVentaDetalle {
    
    public boolean insertar (Session session, Tventadetalle ventaDetalle) throws Exception;
}
