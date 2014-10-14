package models;

public enum Direction {
	Up{
		@Override
		public String toString(){
			return "Up";
		}
	},
	Right{
		@Override
		public String toString(){
			return "Right";
		}
	},
	Down{
		@Override
		public String toString(){
			return "Down";
		}
	},
	Left{
		@Override
		public String toString(){
			return "Left";
		}
	}
}
