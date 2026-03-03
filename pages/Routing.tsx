import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { createStaticNavigation } from '@react-navigation/native';

import { Demo } from './Demo';
import { Home } from './Home';
import { Settings } from './Settings';

export enum Pages {
  Home = 'Home',
  Settings = 'Settings',
  Demo = 'Demo',
}

const RootStack = createNativeStackNavigator({
  initialRouteName: Pages.Demo,
  screens: {
    [Pages.Home]: {
      screen: Home,
      options: { title: 'Welcome', header: null as never },
    },
    [Pages.Demo]: {
      screen: Demo,
      options: { header: null as never },
    },
    [Pages.Settings]: {
      screen: Settings,
    },
  },
});

export const Navigation = createStaticNavigation(RootStack);

export default Navigation;
