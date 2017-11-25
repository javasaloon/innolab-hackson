import React from 'react';
import { 
    Form, 
    Input, 
    DatePicker, 
    Button, 
    Table
} from 'antd';

const FormItem = Form.Item;

class HistoryView extends React.Component {
    render() {

          const dataSource = [{
            key: '1',
            product: "iPhoneX",
            name: "昵称大黄",
            date: "2017-11-25"
          }, {
            key: '2',
            product: "汉堡",
            name: "小黑",
            date: "2017-11-20"
          }];
          
          const columns = [{
            title: '',
            key: 'avatar',
            render: () => (
                <img src="./assets/img/avatar.png" style={{width: "60px"}}/>
          )
          }, {
            title: '名称',
            dataIndex: 'name',
            key: 'name',
          }, {
            title: '奖励',
            dataIndex: 'product',
            key: 'product',
          }, {
            title: '日期',
            dataIndex: 'date',
            key: 'date',
          }];

        return (
            <div style={{width: "80%"}}>
                <div>
                    <Table dataSource={dataSource} columns={columns}/>
                </div>
            </div>
        )
    }
}

export default HistoryView;