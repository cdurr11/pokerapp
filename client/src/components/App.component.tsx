import React, {Component} from 'react';
import '../css/app.css';
import OpponentSidePanel from './OpponentSidePanel.component';
import Table from "./Table.component"
import io from 'socket.io-client';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { actionCreators } from '../redux/actions';
import { AppState } from '../redux/reducers/reducer'

const socket = io.connect("http://localhost:9092"); 

interface ClientMsgType {
  playerName: string,
  action: string,
  raiseAmount: string,
}

interface LoginMsgType { 
  playerName: string,
  providedPassword: string,
}

interface AppProps {
  playerName: string,
}

function mapDispatchToProps(dispatch: any) {
  return bindActionCreators(actionCreators, dispatch)
}

function mapStateToProps(state: AppState): AppProps {
  return {playerName: state.playerName};
}

class App extends React.Component<AppProps,AppState> {
  constructor(props: any) {
    super(props);
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
      playerName: this.props.playerName,
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
        <OpponentSidePanel />
        <div className="ctn-middle">
          <Table
            nonRaiseButtonCallback={this.nonRaiseButtonCallback}
            raiseButtonCallback={this.raiseButtonCallback}
            handleLogin={this.handleLogin}
          />
          <div className="bottom-border-elem"/>
        </div>
        <div className="border-elem"></div>
      </div>
    );
  } 
}
export default connect(mapStateToProps, mapDispatchToProps)(App);
