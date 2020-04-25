import React, {Component} from 'react';
import CommunityCards from "./CommunityCards.component"
import MyHand from "./MyHand.component"
import pokerTableImg from '../static/pokertable.svg';
import '../css/table.css';
import io from 'socket.io-client';
// import io from "socket.io";

const socket = io.connect("http://localhost:9092"); 

interface TableState {

};

interface TableProps {

};

class Table extends React.Component<TableProps, TableState> {
  

  constructor(props: TableProps) {
    super(props);
  }

  cb() {
    console.log("here");
    socket.emit("playerAction", {data: "hi"});
  }

  componentDidMount() {
    socket.on('actionResponse', (data: any): any => {
      console.log(data);
    });
  }
  render(): JSX.Element {
    return (
      <div className="ctn-table">
        <img className="" alt="" src={pokerTableImg}/>
        <button onClick={() => this.cb()} className="call-button mod-unselectable">Call</button>
        <button className="check-button mod-unselectable">Check</button>
        <button className="fold-button mod-unselectable">Fold</button>
        <button disabled={true} className="raise-button mod-unselectable">Raise</button>
        <input placeholder="RAISE AMT" className="raise-form"></input>
        <p className="player-balance mod-unselectable">$100</p>
        <p className="pot mod-unselectable">$20.00</p>
        <MyHand/>
        <CommunityCards/>
      </div>
    );
  }
}
export default Table;