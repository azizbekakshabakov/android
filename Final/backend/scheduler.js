const schedule = require('node-schedule');
const {Usercar} = require('./schemas/usercar');
const { Car } = require('./schemas/car');
const { User } = require('./schemas/user');

async function executeCommand() {
  const usercars = await Usercar.find();
  
  for (let index = 0; index < usercars.length; index++) {
    const usercar = usercars[index];
    console.log(`Usercar ${index + 1}:`, usercar);
    
    const car = await Car.findOne({_id: usercar.carId});
    const user = await User.findOne({_id: usercar.userId});
    
    user.balance -= car.tariff;
    user.save();
  };
}

schedule.scheduleJob('0 0 * * *', () => {
//   console.log('Running scheduled task...');
  executeCommand();
});
