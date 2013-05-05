
Ext.define('Demo.controller.IndexController', {
    extend: 'Ext.app.Controller',
    requires: [
        'Demo.view.IndexView', 
        'Demo.controller.FooController'
    ],
    views: ['IndexView'],
    init: function(viewport) {
        var fooController;
        viewport.setContentView(this.getView('IndexView').create());
        fooController = Ext.create('Demo.controller.FooController');
        fooController.init(viewport);
    }
});
