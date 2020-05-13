import React, {Component} from 'react';
import '../css/app.css';
import OpponentSidePanel from './OpponentSidePanel.component';
import Table from "./Table.component"
import io from 'socket.io-client';

const socket = io.connect("http://localhost:9092"); 

interface AppState {
  playerName: string;
  communityCards: string[];
  myCards: string[];
  playerOrder: string[];
  playerCards: {};
  currentBet: string | undefined;
  spectating: boolean,
  loggedIn: boolean,
}

interface ClientMsgType {
  playerName: string,
  action: string,
  raiseAmount: string,
}

interface LoginMsgType {
  playerName: string,
  providedPassword: string,
}

class App extends React.Component<{},AppState> {
  constructor(props: any) {
    super(props);
    this.state = {
      playerOrder: [],
      playerCards: {},
      playerName: "anonymousPlayer",
      myCards: ["",""],
      communityCards: [],
      currentBet: undefined,
      spectating: true,
      loggedIn: false,
    }

    this.nonRaiseButtonCallback = this.nonRaiseButtonCallback.bind(this);
    this.raiseButtonCallback = this.raiseButtonCallback.bind(this);
  }

  componentDidMount() {
    socket.on('actionResponse', (data: any): any => {
      console.log(data);
    });

    socket.on('loginResponse', (data: any): any => {
      console.log(data);
      if (data.success) {
        this.setState({loggedIn: true});
      }
    });
  }

  nonRaiseButtonCallback(selectedAction: string): void {
    const clientMsg: ClientMsgType = {
      playerName: this.state.playerName,
      action: selectedAction,
      raiseAmount: "",
    };

    socket.emit("playerAction", clientMsg);
  }

  raiseButtonCallback(raiseAmount: string): void {
    const clientMsg: ClientMsgType = {
      playerName: this.state.playerName,
      action: "RAISE",
      raiseAmount: raiseAmount,
    };

    socket.emit("playerAction", clientMsg);
  }

  handleLogin(playerName: string, groupPassword: string): void {

    const clientMsg: LoginMsgType = {
      playerName: playerName,
      providedPassword: groupPassword
    };
    socket.emit("loginAttempt", clientMsg);
  }

  render(): JSX.Element {
    return (
      <div className="ctn-app">
        <OpponentSidePanel 
          playerOrder={this.state.playerOrder}
          playerCards={this.state.playerCards}
          spectating={this.state.spectating}
          playerName={this.state.playerName}
        />
        <div className="ctn-middle">
          <Table 
            nonRaiseButtonCallback={this.nonRaiseButtonCallback}
            raiseButtonCallback={this.raiseButtonCallback}
            handleLogin={this.handleLogin}
            spectating={this.state.spectating}
            loggedIn={this.state.loggedIn}
            communityCards={this.state.communityCards}
            myCards={this.state.myCards}
          />
          <div className="bottom-border-elem"/>
        </div>
        <div className="border-elem"></div>
      </div>
    );
  } 
}
export default App;
