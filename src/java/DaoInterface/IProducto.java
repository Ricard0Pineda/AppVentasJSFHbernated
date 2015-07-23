/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DaoInterface;

import Pojos.Tproducto;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Ricardo
 */
public interface IProducto {
    
    public Tproducto getProductoPorId (Session session, int productId) throws Exception;
    public List<Tproducto> getAllProductos(Session session) throws Exception;
    public Tproducto getProductoPorCB(Session session, String codigoBarras) throws Exception;
            
    
}
