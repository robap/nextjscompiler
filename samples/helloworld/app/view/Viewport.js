Ext.define('Demo.view.Viewport', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [
        {
            region: 'center',
            itemId: 'centerContent',
            xtype: 'panel'
        }
    ],
    setContentView: function(view) {
        this.getComponent('centerContent').removeAll();
        this.getComponent('centerContent').add(view);
    },
    getContentView: function() {
        return this.getComponent('centerContent');
    }
});