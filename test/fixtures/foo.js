
Ext.require('OtherLib.SetByExtRequire');
Ext.require('Sample.Bar'); //dep can be set by ext.require or config.requires

/**
 * test class
 */
Ext.define('Sample.Foo', {
    requires: [
        'Sample.Bar',
        'Sample.Foo' //scenario with circular deps ref
    ],
    uses: [
        'OtherLib.SetByUses'
    ],
    extend: 'Ext.view.View',
    alias: 'widget.sampleFoo',
    items: [
        {
            xtype: 'button'
        }
    ]
});