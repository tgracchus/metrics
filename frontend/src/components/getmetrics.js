import React from 'react'
import Timeline from "./timeline";

class GetMetrics extends React.Component {
    constructor(props) {
        super(props);
        this.state = {metric: '', timestamp: '', timerange: 'LAST_15M', data: []};
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

        fetch(`http://127.0.0.1:8080/timeseries?metric=${this.state.metric}&timeRange=${this.state.timerange}&timestamp=${this.state.timestamp}`, requestOptions)
            .then(response => response.json())
            .then((data) => {
                this.setState({data: data})
            })
            .catch(error => console.log('error', error));

    }

    render() {
        return (
            <div>
                <div>
                    <form onSubmit={this.handleSubmit}>
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
