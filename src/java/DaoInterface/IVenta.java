/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DaoInterface;

import Pojos.Tventa;
import org.hibernate.Session;

/**
 *
 * @author Ricardo
 */
public interface IVenta {
    public boolean insertar(Session session, Tventa venta) throws Exception;
    public Tventa getUltimaVenta(Session session) throws Exception;
}
