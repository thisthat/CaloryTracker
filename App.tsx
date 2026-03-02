/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import { NewAppScreen } from '@react-native/new-app-screen';
import {
  Button,
  StatusBar,
  StyleSheet,
  ScrollView,
  Text,
  useColorScheme,
  View,
} from 'react-native';
import {
  SafeAreaProvider,
  useSafeAreaInsets,
} from 'react-native-safe-area-context';
import Container from './components/Container';
import CalendarModule from './modules/Test'

import {createStaticNavigation, useNavigation} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';

function H() {
  return (<View
      style={{
        backgroundColor: '#09F',
      }}
    >
      <Text>Damn</Text>
    </View>)
}

const RootStack = createNativeStackNavigator({
  screens: {
    Home: {
      screen: AppContent,
      options: {title: 'Welcome', header: null as never},
    },
    Profile: {
      screen: Dummy,
    },
  },
});

const Navigation = createStaticNavigation(RootStack);

function App() {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <SafeAreaProvider>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <Navigation />
    </SafeAreaProvider>
  );
}

function Dummy () {
  return (<Text>Hello dear;</Text>)
}

function AppContent() {
  const safeAreaInsets = useSafeAreaInsets();
  const isDarkMode = useColorScheme() === 'dark';
  const navigation = useNavigation();
  let elms = [];
  for (let i = 0; i <= 43; i++) {
    elms.push(
      <Container key={i} bgColor="#09f">
        <Text key={i}>Element {i}</Text>
      </Container>,
    );
  }
  const onPress = () => {
    console.log("Doing work");
    CalendarModule.createCalendarEvent('foo', 'bar');
    navigation.navigate('Profile');
  }
  return (
    <View
      style={{
        backgroundColor: isDarkMode ? '#f3f3f3' : '#f3f3f3',
        paddingTop: safeAreaInsets.top,
        paddingLeft: safeAreaInsets.left,
        paddingRight: safeAreaInsets.right,
      }}
    >
      <Text>Hi this is a new app and this stays on top!</Text>
      <Button
            title="Click to invoke your native module!"
            color="#841584"
            onPress={onPress}
          />
      <ScrollView
        style={{
          marginBottom: safeAreaInsets.bottom,
        }}
      >
        <View
          style={{
            alignContent: 'center',
            alignItems: 'center',
            width: '100%',
          }}
        >
          {elms}
          <Text>Hi this is a new app </Text>
          {/* Give some extra padding at the bottom */}
          <Text style={{ height: 40 }}></Text>
        </View>
        <NewAppScreen></NewAppScreen>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

export default App;
