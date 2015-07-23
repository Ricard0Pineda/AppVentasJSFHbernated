/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import DaoInterface.IVenta;
import Pojos.Tventa;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ricardo
 */
public class DaoVenta implements IVenta{

    @Override
    public boolean insertar(Session session, Tventa venta) throws Exception {
        session.save(venta);
        return true;
    }

    @Override
    public Tventa getUltimaVenta(Session session) throws Exception {
        Query query = session.createQuery("from Tventa tv order by idVenta");
        query.setMaxResults(1);
        return (Tventa) query.uniqueResult();
    }
    
}
