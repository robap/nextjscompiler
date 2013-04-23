Ext.define('Demo.view.SomethingView', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.something_view',
    items: [
        {
            xtype: 'panel',
            html: "<div>Something</div>"
        }
    ]
});