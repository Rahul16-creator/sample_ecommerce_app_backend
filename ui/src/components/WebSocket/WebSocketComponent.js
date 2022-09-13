import { Component } from "react";

import { WebSocketManager, StompRoutes } from "./WebSocketWrapper";

class WebSocketComponent extends Component {
  constructor(props) {
    super(props);

    this.eventUpdateReceived = this.eventUpdateReceived.bind(this);
    this.state = {
      productCount: 0,
    };
  }

  componentDidMount() {
    this.initializeWebSocket();
  }

  initializeWebSocket = () => {
    WebSocketManager.init();

    this.eventListener = WebSocketManager.registerListener({
      routeKey: StompRoutes.GET_PRODUCT_COUNT,
      routeCallback: this.eventUpdateReceived,
    });
  };

  eventUpdateReceived = (message) => {
    const event = JSON.parse(message.body);

    this.setState({ productCount: event });
  };

  componentWillUnmount() {
    WebSocketManager.unregisterListener(this.challengeListener);
  }

  render() {
    return <p>{this.state.productCount}</p>;
  }
}

export default WebSocketComponent;
