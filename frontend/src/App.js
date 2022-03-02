import './App.css';
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend
} from "recharts";

import React, { Component } from 'react';


class App extends Component {

    state = {
        metrics: []
    }

    componentDidMount() {
        const requestOptions = {
            method: 'GET',
            redirect: 'follow',
            headers : {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        };

        fetch("http://127.0.0.1:8080/timeseries?metric=cpu1&timeRange=LAST_HOUR", requestOptions)
            .then(response => response.json())
            .then((data) => {
                this.setState({ metrics: data })
            })
            .catch(error => console.log('error', error));

    }

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    Metrics frontend
                </header>
                <LineChart
                    width={500}
                    height={300}
                    data={this.state.metrics}
                    margin={{
                        top: 5,
                        right: 30,
                        left: 20,
                        bottom: 5
                    }}
                >
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="key"/>
                    <YAxis/>
                    <Tooltip/>
                    <Legend/>
                    <Line
                        type="monotone"
                        dataKey="value"
                        stroke="#8884d8"
                        activeDot={{r: 8}}
                    />
                </LineChart>
            </div>
        );
    }
}

export default App;
