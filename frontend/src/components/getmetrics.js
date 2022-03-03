import React from 'react'
import Timeline from "./timeline";
import environment from './environment'

class GetMetrics extends React.Component {
    constructor(props) {
        super(props);
        this.state = {metric: '', timestamp: '', timerange: 'LAST_15M', data: [], error: undefined};
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleChange(event) {
        this.setState({[event.target.id]: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        const requestOptions = {
            method: 'GET',
            redirect: 'follow',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        };

        fetch(`http://${environment.backend}:8080/timeseries?metric=${this.state.metric}&timeRange=${this.state.timerange}&timestamp=${this.state.timestamp}`, requestOptions)
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text)
                    })
                }
                return response.json()
            })
            .then(data => {
                data.points.forEach(point => {
                    point["timestamp"] = new Date(point.timestamp).toLocaleString()
                })
                return data
            })
            .then(data => {
                this.setState({error: undefined})
                this.setState({data: data})
            })
            .catch(error => {
                this.setState({error: error.toString()})
                console.log('error', error)
            });

    }

    render() {
        return (
            <div>
                <div>
                    <form onSubmit={this.handleSubmit} >
                        <label>
                            Metric Name:
                            <input id="metric" type="text" value={this.state.metric} onChange={this.handleChange}/>
                        </label>
                        <label>
                            Timestamp:
                            <input id="timestamp" type="text" name="timestamp" value={this.state.timestamp}
                                   onChange={this.handleChange}/>
                        </label>
                        <label>
                            TimeRange
                            <select id="timerange" value={this.state.timerange}
                                    onChange={this.handleChange}>
                                <option value="LAST_15M">Last 15 min</option>
                                <option value="LAST_HOUR">Last Hour</option>
                                <option value="LAST_DAY">Last Day</option>
                            </select>
                            <input type="submit" value="Submit"/>
                            {this.state.error && <div className="App-error"> {this.state.error} </div>}
                        </label>
                    </form>
                </div>
                <div>
                    <Timeline metrics={this.state.data}/>
                </div>
            </div>
        );
    }
}

export default GetMetrics
