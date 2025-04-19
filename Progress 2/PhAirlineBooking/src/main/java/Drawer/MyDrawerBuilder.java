
package Drawer;


import raven.modal.drawer.menu.MenuOption;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;


public class MyDrawerBuilder extends SimpleDrawerBuilder { 
    

    public MyDrawerBuilder(MenuOption menuOption) {
        super(menuOption);
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData();
    }

    @Override
    public SimpleFooterData getSimpleFooterData() {
       return new SimpleFooterData();
    }
    
}
