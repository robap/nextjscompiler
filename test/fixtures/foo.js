
/**
 * test class
 */
Ext.define('Sample.Foo', {
    require: [
        'Sample.Bar'
    ],
    extend: 'Ext.view.View',
    alias: 'widget.sampleFoo',
    items: [
        {
            xtype: 'button'
        }
    ]
});