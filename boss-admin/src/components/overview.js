import React from 'react';
import { 
    Form, 
    Input, 
    DatePicker, 
    Button, 
    Table
} from 'antd';

const FormItem = Form.Item;

class Overview extends React.Component {
    render() {
        const formItemLayout = {
            labelCol: {
              xs: { span: 24 },
              sm: { span: 6 },
            },
            wrapperCol: {
              xs: { span: 24 },
              sm: { span: 14 },
            },
          };

          const dataSource = [{
            key: '1',
            name: 'iPhoneX',
            how: "30s内击败Boss",
            amount: 1
          }, {
            key: '2',
            name: '汉堡',
            how: "DPS达到8000以上",
            amount: 200
          }];
          
          const columns = [{
            title: '奖励名称',
            dataIndex: 'name',
            key: 'name',
          }, {
            title: '获取途径',
            dataIndex: 'how',
            key: 'how',
          }, {
            title: '数量',
            dataIndex: 'amount',
            key: 'amount',
          }, {
            title: '',
            key: 'amount',
            render: () => (
                <a href="#">编辑</a>
            )
          }];

        return (
            <div style={{width: "80%"}}>
                <div style={{display: "flex", alignItems: "center"}}>
                    <img src="./assets/img/boss.png"/>
                    <Form style={{marginLeft: "100px", width: "50%"}}>
                        <FormItem label="血值" {...formItemLayout}>
                            <Input/>
                        </FormItem>
                        <FormItem label="名称" {...formItemLayout}>
                            <Input/>
                        </FormItem>
                        <FormItem label="有效期" {...formItemLayout}>
                            <DatePicker style={{width: "100%"}}/>
                        </FormItem>
                    </Form>
                </div>
                <div style={{marginTop: "20px"}}>
                    <div style={{width: "100%", height: "50px"}}>
                        <Button style={{float: "right"}}>新建</Button>
                    </div>
                    
                    <Table dataSource={dataSource} columns={columns}/>
                </div>
            </div>
        )
    }
}

export default Overview;