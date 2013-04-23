Ext.require('Demo.view.SomethingView');

Ext.define('Demo.view.FooView', {
    extend: 'Demo.view.IndexView',
    items: [
        {
            xtype: 'panel',
            html: "<div>FOO</div>"
        },
        {
            xtype: 'something_view'
        }
    ]
});