Ext.require('Demo.view.FooView');

Ext.define('Demo.controller.FooController', {
    extend: 'Ext.app.Controller',
    require: [
        'Demo.view.FooView'
    ],
    views: ['FooView'],
    init: function(viewport) {
        var fooView = this.getView('Demo.view.FooView').create();
        viewport.getContentView().add(fooView);
    }
});