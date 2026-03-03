import { DateSelector } from '../../components';
import { NewAppScreen } from '@react-native/new-app-screen';
import { View, useColorScheme } from 'react-native';

import { useSafeAreaInsets } from 'react-native-safe-area-context';

export function Demo() {
  const safeAreaInsets = useSafeAreaInsets();
  const isDarkMode = useColorScheme() === 'dark';
  return (
    <View
      style={{
        backgroundColor: isDarkMode ? '#f3f3f3' : '#f3f3f3',
        paddingTop: safeAreaInsets.top,
        paddingLeft: safeAreaInsets.left,
        paddingRight: safeAreaInsets.right,
      }}
    >
      <DateSelector />
      <NewAppScreen></NewAppScreen>
    </View>
  );
}

export default Demo;
