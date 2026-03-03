import { Button, ScrollView, Text, useColorScheme, View } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import { useNavigation } from '@react-navigation/native';

import { Container } from '../../components';
import CalendarModule from '../../modules/Test';
import { Pages } from '../Routing';

export function Home() {
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
    console.log('Doing work');
    CalendarModule.createCalendarEvent('foo', 'bar');
    navigation.navigate(Pages.Settings as never);
  };
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
      </ScrollView>
    </View>
  );
}
