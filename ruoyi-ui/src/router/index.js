import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
import ParentView from '@/components/ParentView';

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    noCache: true                // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: (resolve) => require(['@/views/redirect'], resolve)
      }
    ]
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: '首页',
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
      }
    ]
  },

  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'type/data/:dictId(\\d+)',
        component: (resolve) => require(['@/views/system/dict/data'], resolve),
        name: 'Data',
        meta: { title: '字典数据', icon: '' }
      }
    ]
  },
  {
    path: '/project',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'project',
        component: (resolve) => require(['@/views/project/project/edit'], resolve),
        name: 'AddInfo',
        meta: { title: '项目新建', icon: '', opcode: 'add',noCache: true }
      },
      {
        path: 'project/:projectid(\\d+)',
        component: (resolve) => require(['@/views/project/project/edit'], resolve),
        name: 'EditInfo',
        meta: { title: '项目编辑', icon: '', opcode: 'query',noCache: true }
      },
      {
        path: 'toconfirm/:projectid(\\d+)',
        component: (resolve) => require(['@/views/project/project/edit'], resolve),
        name: 'ConfirmInfo',
        meta: { title: '项目新建审核', icon: '',opcode: 'confirm',noCache: true }
      },
      {
        path: 'toaccept/:projectid(\\d+)',
        component: (resolve) => require(['@/views/project/project/edit'], resolve),
        name: 'AcceptInfo',
        meta: { title: '项目验收', icon: '',opcode: 'toaccept',noCache: true }
      },
      {
        path: 'toacceptanceconfirm/:projectid(\\d+)',
        component: (resolve) => require(['@/views/project/project/edit'], resolve),
        name: 'AcceptanceConfirmInfo',
        meta: { title: '项目验收审核', icon: '',opcode: 'acceptconfirm',noCache: true }
      },
      {
        path: 'finished/:projectid(\\d+)',
        component: (resolve) => require(['@/views/project/project/edit'], resolve),
        name: 'FinishedInfo',
        meta: { title: '项目已完成', icon: '',opcode: 'query',noCache: true }
      },
      {
        path: 'toaddacceptance/:projectid(\\d+)',
        component: (resolve) => require(['@/views/project/project/edit'], resolve),
        name: 'ToAddAcceptanceInfo',
        meta: { title: '项目验收材料补充', icon: '',opcode: 'acceptance',noCache: true }
      }
    ]
  },
  {
    path: '/achieve',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'patent',
        component: (resolve) => require(['@/views/achieve/patent/edit'], resolve),
        name: 'AddPatent',
        meta: {title: '专利新建', icon: '', opcode: 'add', noCache: true}
      },
      {
        path: 'patent/:patentid(\\d+)',
        component: (resolve) => require(['@/views/achieve/patent/edit'], resolve),
        name: 'EditPatent',
        meta: { title: '专利编辑', icon: '', opcode: 'query',noCache: true }
      },
      {
        path: 'patent/confirm/:patentid(\\d+)/:applyid(\\d+)',
        component: (resolve) => require(['@/views/achieve/patent/edit'], resolve),
        name: 'ConfirmPatent',
        meta: { title: '专利审核', icon: '', opcode: 'confirm',noCache: true,}
      }
    ]
  },
  {
    path: '/sheet',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'tijiaoren',
        component: (resolve) => require(['@/views/sheet/tijiaoren/edit'], resolve),
        name: 'AddSheet',
        meta: {title: '拨付单新建', icon: '', opcode: 'add', noCache: true}
      },
      {
        path: 'tijiaoren/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/tijiaoren/edit'], resolve),
        name: 'querySheet',
        meta: { title: '拨付单查看', icon: '', opcode: 'query',noCache: true }
      },
      {
        path: 'audit3/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/tijiaoren/edit'], resolve),
        name: 'Audit3Sheet',
        meta: { title: '拨付单审核', icon: '', opcode: 'audit3',noCache: true }
      },
      {
        path: 'audit4/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/tijiaoren/edit'], resolve),
        name: 'Audit4Sheet',
        meta: { title: '拨付单审核', icon: '', opcode: 'audit4',noCache: true }
      },
      {
        path: 'audit5/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/tijiaoren/edit'], resolve),
        name: 'Audit5Sheet',
        meta: { title: '拨付单审核', icon: '', opcode: 'audit5',noCache: true }
      },
      {
        path: 'audit6/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/tijiaoren/edit'], resolve),
        name: 'Audit6Sheet',
        meta: { title: '拨付单审核', icon: '', opcode: 'audit6',noCache: true }
      },
      {
        path: 'audit7/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/tijiaoren/edit'], resolve),
        name: 'Audit7Sheet',
        meta: { title: '拨付单审核', icon: '', opcode: 'audit7',noCache: true }
      }

    ]
  },
  {
    path: '/contract',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'tijiaoren',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'AddContract',
        meta: {title: '合同新增', icon: '', opcode: 'add', noCache: true}
      },
      {
        path: 'tijiaoren/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'queryContract',
        meta: { title: '合同查看', icon: '', opcode: 'query',noCache: true }
      },
      {
        path: 'paysheet/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/paysheet'], resolve),
        name: 'queryPaysheet',
        meta: { title: '合同拨付单', icon: '', opcode: 'query',noCache: true }
      },
      {
        path: 'audit3/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit3Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit3',noCache: true }
      },
      {
        path: 'audit4/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit4Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit4',noCache: true }
      },
      {
        path: 'audit5/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit5Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit5',noCache: true }
      },
      {
        path: 'audit6/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit6Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit6',noCache: true }
      },
      {
        path: 'audit7/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit7Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit7',noCache: true }
      }
    ]
  },
  {
    path: '/fourtech',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'tijiaoren',
        component: (resolve) => require(['@/views/fourtech/tijiaoren/edit'], resolve),
        name: 'AddFourtechContract',
        meta: {title: '四技合同新增', icon: '', opcode: 'add', noCache: true}
      },
      {
        path: 'tijiaoren/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/fourtech/tijiaoren/edit'], resolve),
        name: 'QueryFourtechContract',
        meta: { title: '四技合同查看', icon: '', opcode: 'query',noCache: true }
      },
      {
        path: 'paysheet/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/paysheet'], resolve),
        name: 'queryPaysheet',
        meta: { title: '合同拨付单', icon: '', opcode: 'query',noCache: true }
      },
      {
        path: 'audit3/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit3Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit3',noCache: true }
      },
      {
        path: 'audit4/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit4Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit4',noCache: true }
      },
      {
        path: 'audit5/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit5Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit5',noCache: true }
      },
      {
        path: 'audit6/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit6Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit6',noCache: true }
      },
      {
        path: 'audit7/:sheetid(\\d+)',
        component: (resolve) => require(['@/views/sheet/contract_tijiaoren/edit'], resolve),
        name: 'Audit7Contract',
        meta: { title: '合同审批', icon: '', opcode: 'audit7',noCache: true }
      }
    ]
  },
  {
    path: '/job',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'log',
        component: (resolve) => require(['@/views/monitor/job/log'], resolve),
        name: 'JobLog',
        meta: { title: '调度日志' }
      }
    ]
  },
  {
    path: '/gen',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/:tableId(\\d+)',
        component: (resolve) => require(['@/views/tool/gen/editTable'], resolve),
        name: 'GenEdit',
        meta: { title: '修改生成配置' }
      }
    ]
  }
]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
