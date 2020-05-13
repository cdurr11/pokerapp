import React, {Component, ChangeEvent} from 'react';
import CommunityCards from "./CommunityCards.component"
import MyHand from "./MyHand.component"
import pokerTableImg from '../static/pokertable.svg';
import '../css/table.css';


interface TableState {
  playerNameValue: string;
  passwordValue: string;
  raiseValue: string;
};

enum FormName {
  USERNAME,
  PASSWORD,
  RAISE,
}

interface TableProps {
  nonRaiseButtonCallback(selectedAction: string): void;
  raiseButtonCallback(raiseAmount: string): void;
  handleLogin(playerName: string, groupPassword: string): void;
  spectating: boolean;
  loggedIn: boolean;
};

class Table extends React.Component<TableProps, TableState> {
  constructor(props: TableProps) {
    super(props);
    this.state = {
      playerNameValue: "",
      passwordValue: "",
      raiseValue: "",
    }
  }

  handleKeypress(event: any, formName:FormName): void {
    if (event.target.value != null) {
      switch (formName) {
        case FormName.USERNAME: {
          this.setState({"playerNameValue": event.target.value});
          break;
        }
        case FormName.PASSWORD: {
          this.setState({"passwordValue": event.target.value});
          break;
        }
        case FormName.RAISE: {
          this.setState({"raiseValue": event.target.value});
          break;
        }
        default:
          throw "Invalid FormName";
      }
    }
    else {
      throw "Form Event Was Null";
    }
  }

  render(): JSX.Element {
    return (
      <div className="ctn-table">
        {!this.props.loggedIn && 
          <div>
            <input placeholder="Username" 
              onChange={(e) => this.handleKeypress(e, FormName.USERNAME)}
              className={"elm-user-form"}></input>
            <input placeholder="Password" 
              onChange={(e) => this.handleKeypress(e, FormName.PASSWORD)}
              className={"elm-group-password-form"}></input>
            <button 
              onClick={() => this.props.handleLogin(this.state.playerNameValue, 
                this.state.passwordValue)}
              className={"elm-login-button mod-unselectable"}>
                Login
            </button>
          </div>
        }

        <img className="" alt="" src={pokerTableImg}/>
        <button onClick={() => this.props.nonRaiseButtonCallback("CALL")}
          className={"call-button mod-unselectable"} disabled={this.props.spectating}>
            Call
        </button>
        <button onClick={() => this.props.nonRaiseButtonCallback("CHECK")} 
          className={"check-button mod-unselectable"} disabled={this.props.spectating}>
            Check
        </button>
        <button onClick={() => this.props.nonRaiseButtonCallback("FOLD")} 
          className={"fold-button mod-unselectable"} disabled={this.props.spectating}>
            Fold
        </button>
        <button onClick={() => this.props.raiseButtonCallback(this.state.raiseValue)} 
          disabled={true} className="raise-button mod-unselectable">Raise</button>
        <input placeholder="RAISE AMT" className="raise-form" 
          disabled={this.props.spectating}>
        </input>
        <p className="player-balance mod-unselectable">$100</p>
        <p className="pot mod-unselectable">$20.00</p>
        <MyHand/>
        <CommunityCards/>
      </div>
    );
  }
}
export default Table;