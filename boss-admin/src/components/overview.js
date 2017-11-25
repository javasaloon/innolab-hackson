import React from 'react';
import { 
    Form, 
    Input, 
    DatePicker, 
    Button, 
    Table,
    Modal,
    Select,
    InputNumber
} from 'antd';

const FormItem = Form.Item;
const Option = Select.Option;

const DefaultData = {
    "name" : "iPhoneX",
    "how" : "30s内击败Boss",
    "amount" : 1
};

class Overview extends React.Component {
    constructor() {
        super();

        this.state = {
            modalVisible : false,
            tempData: DefaultData,
            data: []
        }
    }
    handleClickOk() {
        let data = this.state.data.slice();
        data.push({...this.state.tempData});
        this.setState({
            modalVisible: false,
            data : data.slice()
        });
    }

    handleClickCancel() {
        this.setState({
            tempData: DefaultData,
            modalVisible: false
        });
    }

    handleClickCreate() {
        this.setState({
            modalVisible: true
        });
    }

    handleModalFormChange(key,value) {
        let data = this.state.tempData;
        data[key] = value;
        this.setState({
            tempData: data
        });
    }

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

          const dataSource = this.state.data.slice();
          
          const columns = [{
            title: '掉落名称',
            dataIndex: 'name',
            key: 'name',
          }, {
            title: '获取条件',
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
            <div style={{width: "100%", display: "flex", alignItems: "center", justifyContent: "center", flexDirection: "column"}}>
                <div style={{width: "80%"}}>
                <Modal
                    title="添加掉落"
                    wrapClassName="vertical-center-modal"
                    visible={this.state.modalVisible}
                    onOk={this.handleClickOk.bind(this)}
                    onCancel={this.handleClickCancel.bind(this)}
                    okText="保存"
                    cancelText="取消"
                >
                    <Form style={{}}>
                        <FormItem label="掉落名称" {...formItemLayout}>
                            <Select defaultValue={DefaultData.name} style={{ width: 120 }} onChange={value => {this.handleModalFormChange("name", value)}}>
                                <Option value="iPhoneX">iPhoneX</Option>
                                <Option value="汉堡">汉堡</Option>
                                <Option value="情路套餐">情路套餐</Option>
                                <Option value="电影票买一赠一券">电影票买一赠一券</Option>
                                <Option value="50元代金券">50元代金券</Option>
                            </Select>
                        </FormItem>
                        <FormItem label="获取条件" {...formItemLayout}>
                            <Select defaultValue={DefaultData.how} style={{ width: 120 }} onChange={value => {this.handleModalFormChange("how", value)}}>
                                <Option value="30秒内击败Boss">30秒内击败Boss</Option>
                                <Option value="对Boss造成任意点伤害">对Boss造成任意点伤害</Option>
                                <Option value="和异性组队击败Boss">和异性组队击败Boss</Option>
                                <Option value="单人作战并击败Boss">单人作战并击败Boss</Option>
                                <Option value="最后击杀Boss">最后击杀Boss</Option>
                            </Select>
                        </FormItem>
                        <FormItem label="数量" {...formItemLayout}>
                            <InputNumber defaultValue={DefaultData.amount} onChange={value => {this.handleModalFormChange("amount", value)}}/>
                        </FormItem>
                    </Form>
                </Modal>
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
                        <Button style={{float: "right"}} onClick={this.handleClickCreate.bind(this)}>添加掉落</Button>
                    </div>
                    <Table dataSource={dataSource} columns={columns}/>
                </div>
                </div>
                
            </div>
        )
    }
}

export default Overview;