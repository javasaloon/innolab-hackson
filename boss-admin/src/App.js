import React, { Component } from 'react';
import { Layout, Menu, Icon, Popover, Button } from 'antd';
import logo from './logo.svg';
import './App.css';
import Overview from "./components/overview";
import HistoryView from "./components/history-view";

const {Header, Footer, Sider, Content} = Layout;

class App extends Component {
  constructor() {
    super();

    this.state = {
      collapsed: false,
      menuKey: "1"
    }
  }

  onMenuClicked(data) {
    this.setState({
      menuKey: data.key
    });
  }

  render() {
    var layout = null;
    let key = this.state.menuKey;
    if (key === "1") {
      layout = <Layout>
                  <Header style={{ background: '#fff', padding: 0, fontSize: "20px", textAlign: "center"}}>
                    创建怪兽
                  </Header>
                  <Content style={{ margin: '24px 16px', padding: 24, background: '#fff', minHeight: 280, display: "flex", alignItems: "center", justifyContent: "center" }}>
                    <Overview/>
                  </Content>
                </Layout>
    } else if (key === "2") {
      layout = <Layout>
                  <Header style={{ background: '#fff', padding: 0, fontSize: "20px", textAlign: "center" }}>
                    奖励记录
                  </Header>
                  <Content style={{ margin: '24px 16px', padding: 24, background: '#fff', minHeight: 280, display: "flex", justifyContent: "center"}}>
                    <HistoryView/>
                  </Content>
                </Layout>
    }

    const dropDownContent = (
      <div style={{display:"flex", alignItems: "center"}}>
        <Icon type="logout"/><p style={{marginLeft: "20px"}}>退出</p>
      </div>
    );
    return (
      <div style={{height: "100vh"}}>
        <Layout style={{height: "100%"}}>
          <Header style={{color: "white", fontSize: "24px", display: "flex"}}>
            <div style={{flex: "none"}}>奥特曼打小怪兽</div>
            <div style={{flex: "1", display: "inline-block"}}>
              <div style={{float: "right", display: "flex", alignItems: "center", fontSize: "14px"}}> 
                  <Popover placement="bottom" content={dropDownContent} trigger="click">
                    <Button style={{background: "transparent", border: "none", color: "white", fontSize: "14px"}}icon="user">jacky.cheng@atmdsgs.com <Icon type="down"/></Button>
                  </Popover>
                  <div style={{marginLeft: "50px"}}>商户版</div>
              </div>
            </div>
          </Header>
          <Layout>
            <Sider >
              <Menu theme="dark" mode="inline" defaultSelectedKeys={['1']} onClick={this.onMenuClicked.bind(this)}>
                <Menu.Item key="1">
                  <span>创建怪兽</span>
                </Menu.Item>
                <Menu.Item key="2">
                  <span>奖励记录</span>
                </Menu.Item>
                <Menu.Item key="3">
                  <span>店铺概况</span>
                </Menu.Item>
                <Menu.Item key="4">
                  <span>账户设置</span>
                </Menu.Item>
              </Menu>
            </Sider>
            {layout}
          </Layout>
        </Layout>
      </div>
    );
  }
}

export default App;
