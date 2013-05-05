
Ext.define('Demo.controller.FooController', {
    extend: 'Ext.app.Controller',
    requires: [
        'Demo.view.FooView'
    ],
    views: ['FooView'],
    init: function(viewport) {
        var fooView = this.getView('Demo.view.FooView').create();
        viewport.getContentView().add(fooView);
    }
});