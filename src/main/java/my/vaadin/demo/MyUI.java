package my.vaadin.demo;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.swing.Icon;
import javax.swing.text.IconView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("mytheme")
public class MyUI extends UI {

	private ClienteServicio servicio= ClienteServicio.getInstance();
	private Grid<Cliente> grid = new Grid<>(Cliente.class);
	private TextField filterText = new TextField();
	private ClienteForm form = new ClienteForm(this);
	
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        filterText.setPlaceholder("Filtrado por nombre...");
        filterText.addValueChangeListener( e -> ActualizarListado());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        
        Button ClearFilterTextBtn = new Button(VaadinIcons.CLOSE);
        ClearFilterTextBtn.setDescription("Limpiar el filtro");
        ClearFilterTextBtn.addClickListener(e -> filterText.clear());
        
        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterText, ClearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        Button addClienteBtn = new Button("Nuevo");
        addClienteBtn.addClickListener(e->{
        	grid.asSingleSelect().clear();
        	form.setCliente(new Cliente());
        });
        
        HorizontalLayout toolbar = new HorizontalLayout(filtering, addClienteBtn);
        
        grid.setColumns("nombre","apellido","usuario","email");
        
        HorizontalLayout main = new HorizontalLayout(grid, form);
        main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1); 
        
        
        layout.addComponents(toolbar, main);
        
        form.setVisible(false);
        
        grid.asSingleSelect().addValueChangeListener(e ->
        {
        		if(e.getValue()==null)
        		{
        			form.setVisible(false);
        		}else {
        			form.setCliente(e.getValue());
        		}
        });
               
        ActualizarListado();
        setContent(layout);
    }

	public void ActualizarListado() {
		List<Cliente> clientes =  servicio.findAll(filterText.getValue());
        grid.setItems(clientes);
	}

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
