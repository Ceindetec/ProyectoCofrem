package com.cofrem.transacciones.lib;

/**
 * Clase que hace uso de la libreria Event Bus  de Green Robot
 */
public class GreenRobotEventBus implements EventBus {

    de.greenrobot.event.EventBus eventBus;


    //Constructor de la clase para la inicializacion del bus de eventos
    public GreenRobotEventBus() {
        this.eventBus = de.greenrobot.event.EventBus.getDefault();
    }

    //Metodo publico que maneja la instancia del bus de eventos
    public static GreenRobotEventBus getInstance() {
        return SingletonEventBus.INSTANCE;
    }

    /**
     * Metodo sobrecargad de la interface EventBus para el registro de eventos
     *
     * @param Subscriber
     */
    @Override
    public void register(Object Subscriber) {
        //Llamado al metodo de  registro de la libreria eventbus de GreenRobot
        eventBus.register(Subscriber);
    }

    /**
     * Metodo sobrecargado de la interface EventBus para el retiro del registro de eventos
     *
     * @param Subscriber
     */
    @Override
    public void unregister(Object Subscriber) {

        //Llamado al metodo de retiro del registro de la libreria eventbus de GreenRobot
        eventBus.unregister(Subscriber);
    }

    /**
     * Metodo sobrecargado de la interface EventBus para el manejo de los eventos
     *
     * @param Event
     */
    @Override
    public void post(Object Event) {
        //Llamado al metodo de manejo de eventos de la libreria eventbus de GreenRobot
        eventBus.post(Event);
    }

    //Singleton para el manejo de una instancia unica del bus de eventos
    private static class SingletonEventBus {
        private static final GreenRobotEventBus INSTANCE = new GreenRobotEventBus();
    }
}
