import WebStomp from "webstomp-client";

export const StompRoutes = {
    GET_PRODUCT_COUNT: "GET_PRODUCT_COUNT",
};

const WEBSOCKET_DEBUG = false;

/**
 * Wrapper around websocket functionality for easier management
 * exported as WebSocketManager
 */
class WebSocketWrapper {
    constructor() {
        const appUrl = "http://localhost:8081";
        this.websocketUrl = appUrl.replace("http://", "ws://");
        this.websocketUrl = this.websocketUrl + "/api/v1/ws";

        this.listeners = [];
        this.onOpenListeners = [];
        this.onCloseListeners = [];
        this.socket = null;
        this.stompClient = null;

        this.buildRoute = this.buildRoute.bind(this);
        this.reconnectInterval = null;
    }

    init() {
        let jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTY2MzA3ODIwMCwiZXhwIjoxNjYzMTY0NjAwfQ.if5u8gjpoq8CyDO2sWmokzJr_XioW0TDt_SiTxnzE0w";

        this.socket = new WebSocket(this.websocketUrl, [jwt, "v12.stomp"]);
        this.stompClient = WebStomp.over(this.socket, {
            debug: WEBSOCKET_DEBUG,
            protocols: ["v12.stomp"],
        });

        this.stompClient.connect({},
            () => {
                // connect callback
                this.listeners.forEach((listener) => {
                    this.subscribeRoute_internal(listener);
                });

                this.onOpenListeners.forEach((l) => l());
            },
            () => {
                // close/error callback
                console.log("websocket closed");
                this.listeners.forEach((l) => {
                    l.subscription = null;
                });

                // Invoke all onClose listeners
                this.onCloseListeners.forEach((l) => l());
            }
        );

        // set up continuous attempts to reconnect the websocket if it
        // becomes disconnected
        const restartTimeInMs = 5000;
        if (!this.reconnectInterval) {
            this.reconnectInterval = setInterval(() => {
                if (this.socket?.readyState === WebSocket.CLOSED) {
                    this.stompClient.disconnect();
                    this.stompClient = null;

                    this.init();
                }
            }, restartTimeInMs);
        }
    }

    /**
     * Subscribes the given listener to a route, should only be used internally
     * @param listener
     */
    subscribeRoute_internal(listener) {
        let sub = this.stompClient.subscribe(this.buildRoute(listener.routeKey), listener.routeCallback);
        listener.subscription = sub;
    }

    registerListener(listener) {
        this.listeners.push(listener);
        if (this.stompClient && this.stompClient.connected) {
            this.subscribeRoute_internal(listener);
        }

        return listener;
    }
    unregisterListener(listener) {
        if (listener) {
            let index = this.listeners.findIndex((l) => {
                return l === listener;
            });
            if (index >= 0) {
                if (this.listeners[index].subscription) {
                    this.listeners[index].subscription.unsubscribe();
                }
                this.listeners.splice(index, 1);
            }
        }
    }

    /**
     * Based on the key passed in, creates the route path with the current context
     * Player websocket should always use a route key
     * @param routeKey
     * @returns String that is the full path or the key given if not found
     */
    buildRoute(routeKey) {
        switch (routeKey) {
            case StompRoutes.GET_PRODUCT_COUNT:
                return `/topic/get_product_count`;
            default:
                return routeKey;
        }
    }

}

export const WebSocketManager = new WebSocketWrapper();
