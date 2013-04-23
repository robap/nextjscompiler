/**
 * Main method
 */
Ext.onReady(function() {
    Ext.require('Demo.view.Viewport');
    Ext.require('Demo.controller.IndexController');
    
    Ext.application({
        name: 'Demo',
        autoCreateViewport: false,
        launch: function() {
            var viewport = Ext.create('Demo.view.Viewport');
            var indexController = Ext.create('Demo.controller.IndexController');
            indexController.init(viewport);
        }
    });
});
