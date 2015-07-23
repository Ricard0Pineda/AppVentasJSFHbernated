/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Dao;

import DaoInterface.IProducto;
import Pojos.Tproducto;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ricardo
 */
public class DaoProducto implements IProducto{

    @Override
    public Tproducto getProductoPorId(Session session, int productId) throws Exception {
        return (Tproducto) session.load(Tproducto.class, productId);
    }

    @Override
    public List<Tproducto> getAllProductos(Session session) throws Exception {
        return session.createCriteria(Tproducto.class).list();
    }

    @Override
    public Tproducto getProductoPorCB(Session session, String codigoBarras) throws Exception {
        Query query = session.createQuery("from Tproducto tp where tp.codigoBarras=:codigoBarras");
        query.setParameter("codigoBarras", codigoBarras);
        return (Tproducto) query.uniqueResult();
    }
    
}
