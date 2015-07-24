/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import DaoInterface.IVentaDetalle;
import Pojos.Tventadetalle;
import org.hibernate.Session;

/**
 *
 * @author Ricardo
 */
public class DaoVentaDetalle implements IVentaDetalle{

    @Override
    public boolean insertar(Session session, Tventadetalle ventaDetalle) throws Exception {
        session.save(ventaDetalle);
        return true;
    }
    
}
